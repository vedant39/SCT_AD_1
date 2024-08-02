package Asmr.Calculator.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ApplicationViewMode : ViewModel(){

    private val _firstNum : MutableStateFlow <Double?> = MutableStateFlow(null)
    val firstNum = _firstNum.asStateFlow()

    private val _secondNum : MutableStateFlow <Double?> = MutableStateFlow(null)
    val secondNum = _secondNum.asStateFlow()

    private val _action : MutableStateFlow <String> = MutableStateFlow(" ")
    val action = _action.asStateFlow()

    fun setFirstNum(input:Double){
        _firstNum.update{input}
    }
    fun setSecondNum(input:Double){
        _secondNum.update{input}
    }
    fun setAction(action:String){
        _action.update{ action }
    }

    fun resetAll(){
        _action.update{ "" }
        _firstNum.update { null }
        _secondNum.update { null }

    }


    fun getRes():Double{
        return when (_action.value){
            "-"->{
                _firstNum.value!! - _secondNum.value!!
            }
            "+"->{
                _firstNum.value!! + _secondNum.value!!
            }
            "/"->{
                _firstNum.value!! / _secondNum.value!!
            }
            "x"->{
                _firstNum.value!! * _secondNum.value!!
            }
           else->{
                0.0
            }
        }
    }

}