<template>
  <div id="app">
    <div class="nav">
      <h2>This is Host app</h2>
      <nav>
        <router-link to="/main">Main</router-link> |
        <router-link to="/about">About</router-link> |
        <router-link to="/remote">Remote</router-link>
      </nav>
    </div>
    <router-view/>
    <p>App state: {{ this.$store.state }}</p>
  </div>
</template>


<script>
export default {
  name: 'Main',
}
</script>


<style>
#app {
  width: 500px;
  height: 500px;
  background-color: deepskyblue;
}

.nav {
  text-align: center;
}
</style>