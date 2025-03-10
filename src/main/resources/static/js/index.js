window.addEventListener('load', function () {
    if (!localStorage.getItem("token")) {
        $('.login-card').show();
        $('.mask').show();
    } else {
        $('.login-card').hide();
        $('.mask').hide();
    }

    $('#login-btn').on('click', _.throttle(function () {
        login().then(res => alert('登录成功'));
    }, 5000));

    $('#logout').on('click', logout);

    function logout() {
        localStorage.removeItem('token');
        location.reload();
    }

    async function login() {
        const username = $('#username').val();
        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', $('#password').val());

        const response = await fetch('/chat/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData.toString()
        });

        try {
            const data = await response.json();
            console.log(data);
            if (data.data) {
                localStorage.setItem('token', data.data);
                console.log(data.message);
                location.reload()
            } else {
                console.log('登录失败');
            }
        } catch (error) {
            console.error('解析响应数据时出错:', error);
        }
    }

    function getDate() {
        const date = new Date();
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}/${month}/${day}`;
    }

    function getTime() {
        const date = new Date();
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let second = date.getSeconds();
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        second = second < 10 ? '0' + second : second;
        return `${hours}:${minutes}:${second}`;
    }

    setInterval(() => {
        document.querySelector(".time").innerHTML = getTime();
        document.querySelector(".date").innerHTML = getDate();
    }, 1000)

    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    // 获取 Token 并设置连接头
    const token = localStorage.getItem('token');
    const headers = token ? {Authorization: `Bearer ${token}`} : {};

    stompClient.connect(headers, function (frame) {  // [!code ++]
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (message) {
            console.log('收到消息:', message);
            showMessage(JSON.parse(message.body), true);
            scrollToBottom();
        });
    }, function (error) {
        console.error('Stomp 连接失败:', error);
        if (error.headers.message === '未提供有效的 Token') {
            alert('请先登录！');
            localStorage.removeItem('token');
            location.reload();
        }
    });

    function sendMessage() {
        const messageInput = document.getElementById('msg-input');
        const messageContent = messageInput.value.trim();
        if (messageContent === '') return;

        // 检查 Token 是否存在
        const token = localStorage.getItem('token');
        if (!token) {
            alert('请先登录！');
            return;
        }

        // 构建消息头
        const headers = {
            Authorization: `Bearer ${token}`
        };

        stompClient.send(
            "/app/chat",
            headers,
            JSON.stringify({content: messageContent, sendTime: getDate() + ' ' + getTime()})
        );

        messageInput.value = '';
    }

    function showMessage(message, option = false) {
        const messagesDiv = document.querySelector('.msg-box');
        const messageElement = document.createElement('li');
        messageElement.classList.add('msg');

        const senderElement = document.createElement('div');
        senderElement.classList.add('msg-sender');
        senderElement.textContent = message.sender || '未知用户';

        const contentElement = document.createElement('div');
        contentElement.classList.add('msg-content');
        contentElement.textContent = message.content;

        const timeElement = document.createElement('span');
        timeElement.classList.add('msg-time');
        timeElement.textContent = message.sendTime;

        messageElement.appendChild(senderElement);
        messageElement.appendChild(contentElement);
        messageElement.appendChild(timeElement);

        if (option === true) {
            messagesDiv.append(messageElement);
        } else {
            messagesDiv.prepend(messageElement);
        }
    }

    function getRequestOptions() {
        // 从本地存储中获取 token
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('未找到 token');
            return;
        }

        // 构建包含 Authorization 头部的请求选项
        return {
            method: 'GET',
            headers: {
                // 修正 Authorization 头部格式
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
    }

    function getMessages() {
        // 发起请求
        fetch('/web/messages', getRequestOptions())
            .then(response => {
                if (!response.ok) {
                    throw new Error(`网络响应失败，状态码: ${response.status}`);
                }
                return response.json();
            })
            .then(messages => {
                console.log(messages);
                // 确保 showMessage 函数存在
                messages.forEach(message => {
                    showMessage(message);
                });
            })
            .catch(error => {
                console.error('获取消息失败:', error);
            });
    }

    // function findMessagesById(id) {
    //     fetch(`/web/messages/${id}`, getRequestOptions())
    //         .then(response => {
    //             if (!response.ok) {
    //                 throw new Error('网络响应失败');
    //             }
    //             return response.json();
    //         })
    //         .then(messages => {
    //             messages.forEach(message => {
    //                 showMessage(message);
    //             });
    //         })
    //         .catch(error => {
    //             console.error('根据 ID 获取消息失败:', error);
    //         });
    // }

    function scrollToBottom() {
        const msgBox = $('.msg-box');
        msgBox.animate({
            scrollTop: msgBox.prop('scrollHeight') + $('.msg').outerHeight(true)
        }, 1000)
    }

    const sendButton = document.getElementById('send-btn');
    const throttledClick = _.throttle(() => {
        sendMessage();
    }, 5000); // 500 毫秒的节流时间

    // 绑定发送按钮点击事件
    sendButton.addEventListener('click', throttledClick);

    // 绑定输入框回车事件
    const messageInput = document.getElementById('msg-input');
    messageInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            throttledClick();
        }
    });

    $('#emoji-btn').click(function () {
        location.href = '../emoji.html';
    })

    // 页面加载时获取历史消息
    getMessages();
})