<template>
    <article>
        <div class="title"><a href="#" @click.prevent="changePage('Post')">{{post.title}}</a></div>
        <div class="information">By {{post.user.login}}</div>
        <div class="body">{{post.text}}</div>
        <div class="footer">
            <div class="left">
                <img src="../../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+173</span>
                <img src="../../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                Some time
                <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">{{post.comments.length}}</a>
            </div>
        </div>
        <template v-if="showComments">
            <WriteComment :post="post" :key="post"/>
            <ul class="comments">
                <li v-for="comment in post.comments" :key="comment.id">
                    <Comment :comment="comment"/>
                </li>
            </ul>
        </template>
    </article>
</template>

<script>
    import Comment from "@/components/middle/Comment";
    import WriteComment from "@/components/middle/WriteComment";
    export default {
        name: "Post",
        props: ["post", "showComments"],
        components: {
            Comment,
            WriteComment
        },
        methods: {
            changePage: function (page) {
                this.$root.$emit("onChangePage", page, {post: this.post});
            }
        }
    }
</script>

<style scoped>

</style>