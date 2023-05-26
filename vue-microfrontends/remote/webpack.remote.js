const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const {ModuleFederationPlugin} = require('webpack').container;
const {VueLoaderPlugin} = require("vue-loader");

const deps = require('./package.json').dependencies;

const babelOptions = {
  presets: ['@babel/preset-env'],
  plugins: ['@babel/plugin-syntax-dynamic-import'],
};

module.exports = {
  entry: './src/main.ts',
  mode: 'development',
  output: {
    publicPath: 'auto',
  },
  devServer: {
    host: '127.0.0.1',
    port: 5002,
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        // Javascript files other than vue
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: file => /node_modules/.test(file) && !/\.vue\.js/.test(file),
        options: babelOptions,
      },
      {
        test: /\.scss$/,
        use: ['vue-style-loader', 'css-loader', 'postcss-loader', 'sass-loader'],
      },
      {
        test: /\.ts$/,
        exclude: /node_modules|vue\/src/,
        use: [
          {
            loader: 'babel-loader',
            options: babelOptions,
          },
          {
            loader: 'ts-loader',
            options: {
              appendTsSuffixTo: [/\.vue$/],
              onlyCompileBundledFiles: true,
            },
          },
        ],
      }
    ],
  },
  plugins: [
    new VueLoaderPlugin(),
    new ModuleFederationPlugin({
      name: 'remoteApp',
      filename: 'remoteEntry.js',
      exposes: {
        // './ExternalLoginsView': './src/views/ExternalLoginsView',
        './Feed': './src/feed/Feed.vue',
      },
      shared: {
        // ...deps,
        vue: {
          singleton: true,
          // requiredVersion: deps.vue
          eager: true,
        },
      },
    }),
    new webpack.HotModuleReplacementPlugin(),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, 'index.html'),
      inject: true,
      chunks: ['main'],
      title: 'Vue Facebook buttons',
      hash: true,
    })
  ],
  resolve: {
    alias: {
      vue$: 'vue/dist/vue.esm.js',
      '@': path.resolve(__dirname, './src'),
    },
    extensions: ['*', '.ts', '.js', '.vue', '.json'],
  },
}