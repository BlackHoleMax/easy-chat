window.addEventListener('load', function () {
    // 上传表情图片
    function getRequestOptions() {
        // 从本地存储中获取 token
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('未找到 token');
            return;
        }

        // 构建包含 Authorization 头部的请求选项
        return {
            headers: {
                // 修正 Authorization 头部格式
                'Authorization': `Bearer ${token}`
            }
        }
    }

    const emojiUploadForm = document.getElementById('emoji-upload-form');
    emojiUploadForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const fileInput = document.getElementById('emoji-file');
        const nameInput = document.getElementById('emoji-name');
        const file = fileInput.files[0];
        const name = nameInput.value;

        // 表单校验
        if (!file) {
            console.error('请选择要上传的表情图片');
            return;
        }

        // 假设只允许上传图片文件，可根据实际需求修改允许的文件类型
        const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
        if (!allowedTypes.includes(file.type)) {
            console.error('请上传有效的图片文件（JPEG、PNG 或 GIF）');
            return;
        }

        if (!name || name.trim() === '') {
            console.error('请输入表情名称');
            return;
        }

        // 可以添加更多关于名称的校验，比如长度限制
        const minNameLength = 2;
        const maxNameLength = 10;
        if (name.length < minNameLength || name.length > maxNameLength) {
            console.error(`表情名称长度应在 ${minNameLength} 到 ${maxNameLength} 个字符之间`);
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', name);

        try {
            const options = getRequestOptions();
            options.method = 'POST';
            options.body = formData;
            // 移除手动设置的 Content-Type，让浏览器自动处理
            delete options.headers['Content-Type'];

            const response = await fetch('/emoji-images/upload', options);
            const result = await response.text();
            console.log(result);
            await getEmojiImages()
        } catch (error) {
            console.error('上传表情图片失败:', error);
        }
    });

    // 获取所有表情图片
    async function getEmojiImages() {
        try {
            // 定义请求头
            const headers = {
                'Authorization': `Bearer ${localStorage.getItem('token')}`, // 示例请求头，可按需修改
            };

            // 创建 Request 对象
            const request = new Request('/emoji-images', {
                method: 'GET',
                headers: headers
            });

            const response = await fetch(request);
            const images = await response.json();
            const emojiList = document.getElementById('emoji-list');
            emojiList.innerHTML = '';
            images.forEach(image => {
                // 为单张图片请求创建 Request 对象
                const imgRequest = new Request(`/emoji-images/${image.id}`, {
                    method: 'GET',
                    headers: headers
                });
                const img = document.createElement('img');
                // 为 img 元素的 src 属性使用 Blob URL
                fetch(imgRequest)
                    .then(response => response.blob())
                    .then(blob => {
                        img.src = URL.createObjectURL(blob);
                        img.alt = image.name;
                        emojiList.appendChild(img);
                    })
                    .catch(error => {
                        console.error('获取单张表情图片失败:', error);
                    });
            });
        } catch (error) {
            console.error('获取表情图片失败:', error);
        }
    }

    // 页面加载时获取所有表情图片
    getEmojiImages();
});