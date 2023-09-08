<template>
  <div id="app">
    <div class="nav">
      <h2>This is remote app</h2>
      <p>
        <router-link to="/feed">Feed</router-link> |
        <router-link to="/logins">Login</router-link>
      </p>
    </div>
    <router-view/>
    <p>App state: {{ this.$store.state }}</p>
  </div>
</template>


<style scoped>
#app {
  width: 400px;
  height: 400px;
  background-color: darkorange;
}

.nav {
  text-align: center;
}
</style>
