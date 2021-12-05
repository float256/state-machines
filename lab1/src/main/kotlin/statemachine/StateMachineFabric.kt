package statemachine

import java.io.InputStream

interface StateMachineFabric {
    fun parse(inputStream: InputStream): StateMachine<out Any>
    fun parse(stateMachine: StateMachine<out Any>): StateMachine<out Any>
}