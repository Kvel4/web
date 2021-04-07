<template>
    <div class="middle">
        <Sidebar :posts="viewPosts"/>
        <main>
            <Index v-if="page === 'Index'" :posts="posts"/>
            <Users v-if="page === 'Users'" :users="users"/>
            <WritePost v-if="page === 'WritePost'"/>
            <Post v-if="page === 'Post'" :post="args.post" :show-comments="true"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
        </main>
    </div>
</template>

<script>
import Sidebar from "@/components/sidebar/Sidebar";
import Index from "@/components/middle/Index";
import Users from "@/components/middle/Users";
import Enter from "@/components/middle/Enter";
import Register from "@/components/middle/Register";
import Post from "@/components/middle/Post";
import WritePost from "@/components/middle/WritePost";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index",
            args: {}
        }
    },
    components: {
        Enter,
        Register,
        Index,
        Users,
        WritePost,
        Post,
        Sidebar,
    },
    props: ["posts", "users"],
    computed: {
        viewPosts: function () {
            return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
        }
    }, beforeCreate() {
        this.$root.$on("onChangePage", (page, args = {}) => {
            this.page = page;
            this.args = args;
        })
    }
}
</script>

<style scoped>

</style>