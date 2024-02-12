
# Harminder's Edgeville Yew Chopper RuneLite Plugin

This RuneLite plugin is designed to enhance the player's experience by providing additional overlays and functionalities within the game. The plugin captures various game attributes and sends them to an API, as well as draws overlays on specific in-game objects and UI elements.
<img src="https://i.imgur.com/8MZB8I8.png">
<img src="https://i.imgur.com/gEheZg7.png">
## Features

- **GameObject Overlay**: Draws overlays on specific game objects within the game world, such as Yew trees and Bank booths, and highlights them in distinct colors.
- **Bank Button Overlay**: Draws overlays on specific UI elements within the bank interface, like the deposit button, and highlights them with solid colors.
- **Player Status Update**: Captures and logs the player's current status, including woodcutting status, movement status, idle status, and current tile location. Additionally, it checks if the bank is open.
- **Inventory Item Tracking**: Tracks and logs the items in the player's inventory, sending this information to a predefined API endpoint.

## Installation

1. Clone the repository or download the plugin files.
2. Place the plugin files in the RuneLite plugins directory.
3. Restart the RuneLite client to detect the new plugin.

## Usage

Once installed, the plugin will automatically start when the RuneLite client is launched. It will perform the following actions:

- Highlight specified game objects and UI elements in the game.
- Send player status and inventory data to the configured API endpoint at regular intervals.

## Configuration

The plugin does not require any additional configuration. However, it's crucial to ensure that the API endpoint (`[localhost:42069](http://localhost:42069)`) is correctly set up and running to receive data sent by the plugin.

## Contributing

Contributions to the plugin are welcome. Please adhere to the following steps for contributing:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes with clear commit messages.
4. Open a pull request with a detailed description of your changes.

## License

This plugin is open-source software licensed under the MIT License.

## Support

For support, questions, or more information about the plugin, please visit [Harminder's personal site](https://harminder.dev).

## Disclaimer

This plugin is developed independently and is not officially endorsed by or affiliated with RuneLite or Old School RuneScape.

