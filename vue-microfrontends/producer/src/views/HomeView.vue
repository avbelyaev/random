<template>
  <main>
    <div class="home">
      <p>Welcome Home</p>
      <Feed />

      <v-facebook-login
          :app-id="'349506013672626'"
          :login-options="{ scope: 'instagram_basic', }"
          @sdk-init="handleIgSdkInit"
      >
        <template v-slot:login>Login with Instagram app 3495</template>
      </v-facebook-login>
    </div>
  </main>
</template>

<script>
import Feed from "../components/Feed";
import VFacebookLogin from 'vue-facebook-login-component'

export default {
  name: 'HomeView',
  components: {
    Feed,
    VFacebookLogin
  },

  data() {
    return {
      igScope: {},
    }
  },

  methods: {
    handleIgSdkInit({scope}) {
      console.log('init IG');
      this.igScope = scope;
    },
  }
}
</script>

<style lang="scss">
.home {
  background-color: deepskyblue;
  height: 200px;
}
</style>