function register() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const inviteCode = document.getElementById("invite-code").value;

    if (!username.trim() || !password.trim()) {
        document.getElementById("message").innerText = "用户名和密码不能为空！";
        return;
    }

    if (!inviteCode.trim()) {
        document.getElementById("message").innerText = "邀请码不能为空！";
        return;
    }

    const usernamePattern = new Pattern(
        document.getElementById("username"),
        /^(?=.*[a-zA-Z])[a-zA-Z\u4e00-\u9fa5\u3040-\u309f\u30a0-\u30ff]{5,}$/,
        "请输入有效的用户名，至少五位，包含英文字母或中日文字符"
    );
    const passwordPattern = new Pattern(
        document.getElementById("password"),
        /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\W_!@#$%^&*`~()-+=]+$)(?![0-9\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\W_!@#$%^&*`~()-+=]/,
        "密码必须包含大写字母，小写字母，数字，特殊符号任意三项"
    );
    const inviteCodePattern = new Pattern(
        document.getElementById("invite-code"),
        /^[a-zA-Z0-9]{6}$/,
        "邀请码为六位英文加数字"
    );

    if (!usernamePattern.validate()) return;
    if (!passwordPattern.validate()) return;
    if (!inviteCodePattern.validate()) return;

    const formData = new URLSearchParams();
    formData.append("username", username);
    formData.append("password", password);
    formData.append("code", inviteCode);

    fetch("/chat/user/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: formData,
    })
        .then((response) => response.json())
        .then((data) => {
            if (!data.code) {
                document.getElementById("message").innerText = "注册成功，正在返回登录";
                document.getElementById("message").style.color = "green";
                setTimeout(() => {
                    location.href = "../index.html";
                }, 1000);
            } else {
                document.getElementById("message").innerText =
                    data.message || "注册失败";
            }
        })
        .catch((error) => {
            document.getElementById("message").innerText =
                "请求出错：" + error.message;
        });
}
