package com.cheeze.pizza.pizzacheeze.types

class ShipLocation(var name: String = "", var price: Double = 0.0) {
    override fun toString(): String {
        return name
    }
}