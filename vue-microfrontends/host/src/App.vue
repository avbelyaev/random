<template>
  <div id="app">
    <div>
      <h2>This is Host app</h2>
      <nav>
        <router-link to="/main">Main</router-link> |
        <router-link to="/about">About</router-link> |
        <router-link to="/remote">Remote</router-link>
      </nav>
    </div>

    <router-view/>
  </div>
</template>


<script>


export default {
  name: 'Main',
}


</script>