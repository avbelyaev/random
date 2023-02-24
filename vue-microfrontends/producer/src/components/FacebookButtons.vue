<template>
  <div class="buttons">
    <p>Login with Facebook</p>
    <v-facebook-login
      :app-id="'966242223397117'"
      :login-options="{ scope: 'pages_messaging', }"
      @sdk-init="handleFbSdkInit"
    >
      <template v-slot:login>Login with Facebook app 9662</template>
    </v-facebook-login>

    <p>Login with Instagram</p>
    <v-facebook-login
      :app-id="'349506013672626'"
      :login-options="{ scope: 'instagram_basic', }"
      @sdk-init="handleIgSdkInit"
    >
      <template v-slot:login>Login with Instagram app 3495</template>
    </v-facebook-login>
  </div>
</template>

<script>
import VFacebookLogin from 'vue-facebook-login-component'

export default {
  name: 'FacebookButtons',

  components: {
    VFacebookLogin,
  },

  data() {
    return {
      fbScope: {},
      igScope: {},
    }
  },

  methods: {
    handleFbSdkInit({scope}) {
      console.log('init FB');
      this.fbScope = scope;
    },
    handleIgSdkInit({scope}) {
      console.log('init IG');
      this.igScope = scope;
    },
  }
}
</script>

<style lang="scss">
.buttons {
  background-color: orange;
}
</style>
