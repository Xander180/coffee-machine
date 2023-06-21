package machine

fun main() {
    val machineContents = MachineContents(400, 540, 120, 9, 550)
    val coffeeMaker = CoffeeMachine(machineContents)
    coffeeMaker.runMachine()
}

data class MachineContents(var water: Int, var milk: Int, var coffeeBeans: Int, var cups: Int, var money: Int)

enum class CoffeeTypes(
    val coffeeName: String,
    private val water: Int,
    private val milk: Int,
    private val coffeeBeans: Int,
    private val cups: Int,
    private val money: Int
) {
    ESPRESSO("Espresso", 250, 0, 16, 1, 4),
    LATTE("Latte", 350, 75, 20, 1, 7),
    CAPPUCCINO("Cappuccino", 200, 100, 12, 1, 6);

    fun buyCoffee(ingredients: MachineContents): MachineContents {
        when {
            ingredients.water < this.water -> println("Sorry, not enough water\n")
            ingredients.milk < this.milk -> println("Sorry, not enough milk\n")
            ingredients.coffeeBeans < this.coffeeBeans -> println("Sorry, not enough coffee beans\n")
            ingredients.cups < this.cups -> println("Sorry, not enough cups\n")
            else -> {
                println("I have enough resources, making you a coffee\n")
                ingredients.coffeeBeans -= this.coffeeBeans
                ingredients.water -= this.water
                ingredients.milk -= this.milk
                ingredients.cups -= this.cups
                ingredients.money += this.money
            }
        }
        return ingredients
    }
}

enum class MachineStatus {
    ACTION, // choosing an action
    BUY, // choosing a variant of coffee
    INGREDIENTS, // displaying ingredient status
    FILL, // filling machine with ingredients
    TAKE, // taking money from machine
    EXIT, // switching off machine
    ERROR // unknown action
}

class CoffeeMachine(private var machineContents: MachineContents) {
    private var status: MachineStatus = MachineStatus.ACTION

    fun runMachine() {
        while (status != MachineStatus.EXIT) {
            when (status) {
                MachineStatus.ACTION -> this.action()
                MachineStatus.BUY -> this.buy()
                MachineStatus.FILL -> this.fill()
                MachineStatus.INGREDIENTS -> this.ingredientStatus()
                MachineStatus.TAKE -> this.take()
                else -> println("Invalid input")
            }
        }
    }

    private fun action() {
        println("Write action (buy, fill, take, remaining, exit):")
        val choice = readln()
        this.status = when (choice) {
            "buy" -> MachineStatus.BUY
            "fill" -> MachineStatus.FILL
            "take" -> MachineStatus.TAKE
            "remaining" -> MachineStatus.INGREDIENTS
            "exit" -> MachineStatus.EXIT
            else -> MachineStatus.ERROR
        }
    }

    private fun take() {
        println("I gave you $${machineContents.money} \n")
        machineContents.money = 0 // empties cash register
        this.status = MachineStatus.ACTION
    }

    private fun ingredientStatus() {
        println(
            """
        
        The coffee machine has:
        ${machineContents.water} ml of water
        ${machineContents.milk} ml of milk
        ${machineContents.coffeeBeans} g of coffee beans
        ${machineContents.cups} disposable cups
        $${machineContents.money} of money
        
    """.trimIndent()
        )
        this.status = MachineStatus.ACTION
    }

    private fun fill() {
        println("\nWrite how many ml of water you want to add:")
        machineContents.water += readln().toInt()
        println("Write how many ml of milk you want to add:")
        machineContents.milk += readln().toInt()
        println("Write how many grams of coffee beans you want to add:")
        machineContents.coffeeBeans += readln().toInt()
        println("Write how many disposable cups you want to add:")
        machineContents.cups += readln().toInt()
        println()
        this.status = MachineStatus.ACTION
    }

    private fun buy() {
        println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
        val choice = readln()
        this.status = MachineStatus.ACTION
        machineContents = when (choice) {
            "1" -> CoffeeTypes.ESPRESSO.buyCoffee(this.machineContents)
            "2" -> CoffeeTypes.LATTE.buyCoffee(this.machineContents)
            "3" -> CoffeeTypes.CAPPUCCINO.buyCoffee(this.machineContents)
            else -> return
        }

    }

}