<template>
  <div id="app">
    <div class="nav">
      <p>This is remote app</p>
      <p>
        <router-link to="/feed">Feed</router-link>
        <router-link to="/logins">Login</router-link>
      </p>
    </div>
    <router-view></router-view>
  </div>
</template>

<style lang="scss">
#app {
  width: 300px;
}

.nav {
  background-color: hotpink;
  text-align: center;
}
</style>
