<template>
    <div class="form">
        <form @submit.prevent="onWriteComment">
            <div class="field">
                <div class="name">
                    <label for="text">Text</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text" v-model="text"></textarea>
                </div>
            </div>
            <div class="error">{{ error }}</div>
            <div class="button-field">
                <input type="submit" value="Write">
            </div>
        </form>
    </div>
</template>

<script>
export default {
    name: "WriteComment",
    props: ["post"],
    data: function () {
        return {
            text: "",
            error: ""
        }
    },
    methods: {
        onWriteComment: function () {
            this.error = "";
            this.$root.$emit("onWriteComment", this.post, this.text);
        }
    },
    beforeMount() {
        this.$root.$on("onWriteCommentValidationError", error => this.error = error);
    }
}
</script>

<style scoped>

</style>