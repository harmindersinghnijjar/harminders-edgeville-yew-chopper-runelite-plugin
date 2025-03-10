from queue import Empty
from typing import List
from fastapi import FastAPI
from pydantic import BaseModel


class PlayerStatusModel(BaseModel):
    WoodcuttingStatus: str
    MovementStatus: str
    IdleStatus: str
    TileLocation: str
    BankOpen: bool


class InventoryItemModel(BaseModel):
    SlotNumber: int
    ItemName: str
    Quantity: int


app = FastAPI()

player_status = {
    "woodcutting_status": "Not Woodcutting",
    "movement_status": "Not Moving",
    "idle_status": "Idle",
    "tile_location": "X: 0, Y: 0, Z: 0",
    "bank_status": False,
}

inventory_items = {}


@app.post("/updatePlayerStatus")
async def update_player_status(status: PlayerStatusModel):
    print(f"Received player status: {status.json()}")
    player_status["woodcutting_status"] = status.WoodcuttingStatus
    player_status["movement_status"] = status.MovementStatus
    player_status["idle_status"] = status.IdleStatus
    player_status["tile_location"] = status.TileLocation
    player_status["bank_status"] = status.BankOpen
    return {"status": "Player status updated."}


@app.post("/updateInventory")
async def update_inventory(inventory: List[InventoryItemModel]):
    global inventory_items
    inventory_items = {}  # Resetting the inventory before updating
    # for item in inventory and != Empty Slot:
    for item in inventory:
        if item.ItemName != "Empty Slot":
            inventory_items[item.SlotNumber] = {
                "item_name": item.ItemName,
                "quantity": item.Quantity,
            }
    print(f"Inventory updated: {inventory_items}")
    return {"status": "Inventory updated."}


@app.get("/getPlayerStatus")
async def get_player_status():
    print(f"Returning player status: {player_status}")
    return player_status

@app.get("/getInventoryItems")
async def get_inventory_items():
    print(f"Returning inventory items: {inventory_items}")
    return inventory_items


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="127.0.0.1", port=42069)
