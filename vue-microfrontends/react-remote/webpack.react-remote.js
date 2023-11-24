const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const {ModuleFederationPlugin} = require('webpack').container;


module.exports = {
    entry: path.resolve(__dirname, './src/index.js'),
    mode: 'development',
    output: {
        publicPath: 'auto',
    },
    devServer: {
        host: '127.0.0.1',
        port: 5003,
    },
    optimization: {
        minimize: false,
    },
    resolve: {
        extensions: ['.jsx', '.js', '.json']
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            '@babel/preset-env',
                            ['@babel/preset-react', {"runtime": "automatic"}]
                        ]
                    }
                },
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.svg$/,
                use: [
                    {
                        loader: 'svg-url-loader',
                        options: {
                            limit: 10000,
                        },
                    },
                ],
            }
        ],
    },
    plugins: [
        new ModuleFederationPlugin({
            name: 'reactRemoteApp',
            filename: 'remoteEntry.js',
            exposes: {
                './Logo': './src/Logo',
            },
            shared: {
                react: {
                    singleton: true,
                    eager: true
                },
                "react-dom": {
                    singleton: true,
                    eager: true
                },
            }
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, './src/index.html'),
        }),
    ]
}