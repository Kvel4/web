<template>
    <div class="posts">
        <Post v-for="post in posts" :key="post.id" :post="post" :show-comments="false"/>
    </div>
</template>

<script>

import Post from "@/components/middle/Post";

export default {
    name: "Index",
    props: ["posts"],
    components: {
        Post
    }
}
</script>

<style scoped>

</style>