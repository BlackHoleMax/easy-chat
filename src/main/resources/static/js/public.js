document.addEventListener('DOMContentLoaded', () => {
    // 检测系统主题并切换样式
    function applyTheme(theme) {
        document.body.classList.toggle('dark-mode', theme === 'dark');
        const emojiForm = document.getElementById('emoji-upload-form');
        if (emojiForm) {
            emojiForm.classList.toggle('dark-mode', theme === 'dark');
        }
    }

    // 根据时间设置主题
    function setThemeBasedOnTime() {
        const hour = new Date().getHours();
        const theme = (hour >= 18 || hour < 6) ? 'dark' : 'light';
        applyTheme(theme);
    }

    // 检测系统主题
    const darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    applyTheme(darkModeMediaQuery.matches ? 'dark' : 'light');

    // 监听系统主题变化
    darkModeMediaQuery.addEventListener('change', (e) => {
        applyTheme(e.matches ? 'dark' : 'light');
    });

    // 根据时间设置主题
    setThemeBasedOnTime();
});