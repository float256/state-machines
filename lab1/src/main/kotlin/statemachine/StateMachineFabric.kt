package statemachine

import java.io.InputStream

interface StateMachineFabric {
    fun create(inputStream: InputStream): StateMachine<out Any>
    fun create(stateMachine: StateMachine<out Any>): StateMachine<out Any>
}