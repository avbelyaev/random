<template>
  <div id="app">
    <div>
      <p>This is local app</p>

      <nav>
        <router-link to="/home">Home</router-link>
        <router-link to="/about">About</router-link>
        <router-link to="/remote">Remote</router-link>
      </nav>
    </div>

    <router-view/>
  </div>
</template>
