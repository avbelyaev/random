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
    }
  },

  methods: {
    handleFbSdkInit({scope}) {
      console.log('init FB');
      this.fbScope = scope;
    },
  }
}
</script>

<style lang="scss">
.buttons {
  background-color: orange;
}
</style>
