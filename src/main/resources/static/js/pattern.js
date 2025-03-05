class Pattern {
    constructor(input, pattern = /^$/, msg = "") {
        this.input = input;
        this.pattern = pattern;
        this.msg = msg;
    }

    validate() {
        const inputValue = this.input.value.trim();
        if (!this.pattern.test(inputValue)) {
            $('#message').html(this.msg);
            return false;
        }
        return true;
    }
}