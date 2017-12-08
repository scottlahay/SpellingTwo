package scott.spelling.presenter

import android.content.DialogInterface

open class BasicDialogDetails(val title: String, val text: String) : DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface, which: Int) {
    }
}

class Hint(val viewModel: SpellingViewModel, title: String, text: String) : BasicDialogDetails(title, text) {
    override fun onClick(dialog: DialogInterface, which: Int) {
        viewModel.captureTyping.value = true
        viewModel.showCheckMark.value = false
        viewModel.updateText("")
    }
}

class Finished(val viewModel: SpellingViewModel, title: String, text: String) : BasicDialogDetails(title, text) {
    override fun onClick(dialog: DialogInterface, which: Int) {
        viewModel.goToLandingPage()
    }
}
