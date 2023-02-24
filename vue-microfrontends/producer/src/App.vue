<template>
  <div id="app">
    <div class="nav">
      <p>This is remote producer app</p>
      <p>
        <router-link to="/home">Go home</router-link>
        <router-link to="/extlogins">Go login</router-link>
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
