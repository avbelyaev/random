<template>
  <div class="logins">
    <p>External Logins</p>
    <facebook-buttons/>
  </div>
</template>

<script>
import FacebookButtons from "@/components/FacebookButtons";

export default {
  name: 'ExternalLoginsView',

  components: {FacebookButtons},

  data() {
    return {}
  },
}
</script>

<style lang="scss">
.logins {
  background-color: darkgray;
}
</style>
