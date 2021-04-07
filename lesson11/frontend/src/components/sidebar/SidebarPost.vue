<template>
    <section>
        <div class="header">
            {{ post.title }}
        </div>
        <div class="body">
            {{ post.text }}
        </div>
        <div class="footer">
            <a href="#" @click.prevent="changePage('Post')">View all</a>
        </div>
    </section>
</template>

<script>
export default {
    name: "SidebarPost",
    props: ["post"],
    methods: {
        changePage: function (page) {
            this.$root.$emit("onChangePage", page, {post: this.post});
        }
    }
}
</script>

<style scoped>
section {
    margin-bottom: 1rem;
}
</style>