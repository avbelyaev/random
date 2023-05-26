<template>
  <div id="app">
    <div>
      <p>This is host app</p>
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