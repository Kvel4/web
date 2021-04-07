<template>
    <div class="comment">
        <div class="info">By {{comment.user.login}} {{comment.creationTime}}</div>
        <div class="text">{{comment.text}}</div>
    </div>
</template>

<script>
export default {
    name: "Comment",
    props: ["comment"]
}
</script>

<style scoped>

</style>