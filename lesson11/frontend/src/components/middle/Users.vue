<template>
    <div class="users datatable">
        <div class="caption">Users</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in users" v-bind:key="user.id">
                <td class="userId" > {{ user.id }}</td>
                <td class="userLogin"> {{ user.login }}</td>
                <td class="userName"> {{ user.name }}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
export default {
    name: "Users",
    props: ["users"]
}
</script>

<style scoped>

</style>