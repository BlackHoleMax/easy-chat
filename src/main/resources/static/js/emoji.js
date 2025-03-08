window.addEventListener('load', function () {
    // 上传表情图片
    function getRequestOptions() {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('未找到 token');
            return;
        }
        return {
            headers: {
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

        if (!file) {
            console.error('请选择要上传的图片');
            return;
        }

        const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
        if (!allowedTypes.includes(file.type)) {
            console.error('请上传有效的图片文件（JPEG、PNG 或 GIF）');
            return;
        }

        if (!name || name.trim() === '') {
            console.error('请输入图片名称');
            return;
        }

        const minNameLength = 2;
        const maxNameLength = 10;
        if (name.length < minNameLength || name.length > maxNameLength) {
            console.error(`图片名称长度应在 ${minNameLength} 到 ${maxNameLength} 个字符之间`);
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', name);

        try {
            const options = getRequestOptions();
            options.method = 'POST';
            options.body = formData;
            delete options.headers['Content-Type'];

            const response = await fetch('/emoji-images/upload', options);
            const result = await response.text();
            console.log(result);
            await getEmojiImages();

            fileInput.value = '';
            nameInput.value = '';
        } catch (error) {
            console.error('上传图片失败:', error);
        }
    });

    async function getEmojiImages() {
        try {
            const headers = {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
            };

            const request = new Request('/emoji-images', {
                method: 'GET',
                headers: headers
            });

            const response = await fetch(request);
            const images = await response.json();
            const emojiList = document.getElementById('emoji-list');
            emojiList.innerHTML = '';
            images.forEach(image => {
                const imgRequest = new Request(`/emoji-images/compressed/${image.id}`, {
                    method: 'GET',
                    headers: headers
                });
                const img = document.createElement('img');
                fetch(imgRequest)
                    .then(response => response.blob())
                    .then(blob => {
                        img.src = URL.createObjectURL(blob);
                        img.alt = image.name;
                        emojiList.appendChild(img);
                    })
                    .catch(error => {
                        console.error('获取单张图片失败:', error);
                    });
            });
        } catch (error) {
            console.error('获取图片失败:', error);
        }
    }

    getEmojiImages();
});