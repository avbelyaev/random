<template>
  <main>
    <div class="home">
      <p>News feed</p>
      <Feed />
    </div>
  </main>
</template>

<script>
import Feed from "./Feed";
import VFacebookLogin from 'vue-facebook-login-component'

export default {
  name: 'HomeView',
  components: {
    Feed,
  },

  data() {
    return {
      igScope: {},
    }
  },

}
</script>

<style lang="scss">
.home {
  background-color: deepskyblue;
  height: 200px;
}
</style>