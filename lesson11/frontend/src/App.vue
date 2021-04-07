<template>
    <div id="app">
        <Header :user="user"/>
        <Middle :posts="posts" :users="users"/>
        <Footer/>
    </div>
</template>

<script>
import Header from "./components/Header";
import Middle from "./components/Middle";
import Footer from "./components/Footer";
import axios from "axios"

export default {
    name: 'App',
    components: {
        Footer,
        Middle,
        Header
    },
    data: function () {
        return {
            user: null,
            users: [],
            posts: []
        }
    },
    beforeMount() {
        if (localStorage.getItem("jwt") && !this.user) {
            this.$root.$emit("onJwt", localStorage.getItem("jwt"));
        }

        axios.get("/api/1/users").then(response => {
          this.users = response.data;
        })

        axios.get("/api/1/posts").then(response => {
            this.posts = Object.values(response.data)
        });
    },
    beforeCreate() {
        this.$root.$on("onRegister", (name, login, password) => {
            axios.post("/api/1/users", {
                    name, login, password
            }).then(() => {
                this.$root.$emit("onEnter", login, password);
            }).catch(error => {
                this.$root.$emit("onRegisterValidationError", error.response.data);
            });
        });

        this.$root.$on("onWritePost", (title, text) => {
            const jwt = localStorage.getItem("jwt")
            axios.post("/api/1/posts", {
                title, text, jwt
            }).then(()=> {
                axios.get("/api/1/posts").then(response => {
                    this.posts = Object.values(response.data);
                });
                this.$root.$emit("onChangePage", "Index");
            }).catch(error => {
                this.$root.$emit("onWritePostValidationError", error);
            })
        })

        this.$root.$on("onWriteComment", (post, text) => {
            const jwt = localStorage.getItem("jwt")
            axios.post(`/api/1/post/${post.id}`, {
                text, jwt
            }).then(()=> {
                axios.get(`/api/1/posts`).then(
                    response => {
                        this.posts = Object.values(response.data);
                    }
                )
                axios.get(`/api/1/post/${post.id}`).then(
                    response => {
                        this.$root.$emit("onChangePage", "Post", {post: response.data})
                    }
                )
            }).catch(error => {
                this.$root.$emit("onWriteCommentValidationError", error);
            })
        })

        this.$root.$on("onEnter", (login, password) => {
            if (password === "") {
                this.$root.$emit("onEnterValidationError", "Password is required");
                return;
            }

            axios.post("/api/1/jwt", {
                login, password
            }).then(response => {
                this.$root.$emit("onJwt", response.data);
            }).catch(error => {
                this.$root.$emit("onEnterValidationError", error.response.data);
            });
        });

        this.$root.$on("onJwt", (jwt) => {
            localStorage.setItem("jwt", jwt);

            axios.get("/api/1/users/auth", {
                params: {
                    jwt
                }
            }).then(response => {
                this.user = response.data;
                this.$root.$emit("onChangePage", "Index");
            }).catch(() => this.$root.$emit("onLogout"))
        });

        this.$root.$on("onLogout", () => {
            localStorage.removeItem("jwt");
            this.user = null;
        });

    }
}
</script>

<style>
#app {

}
</style>
