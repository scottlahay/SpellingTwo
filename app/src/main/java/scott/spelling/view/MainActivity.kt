package scott.spelling.view

import android.R.anim.slide_in_left
import android.R.anim.slide_out_right
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.inputmethodservice.Keyboard
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity.CENTER
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chibatching.kotpref.Kotpref
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.activity_spelling.*
import kotlinx.android.synthetic.main.landing_page.*
import kotlinx.android.synthetic.main.spelling_test_page.*
import scott.spelling.R
import scott.spelling.R.layout.activity_spelling
import scott.spelling.R.style.MyAlertDialogStyle
import scott.spelling.presenter.BasicDialogDetails
import scott.spelling.presenter.MainPage
import scott.spelling.presenter.SpellingViewModel

@GlideModule
class MyAppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity() {

    lateinit var keyboard: Keyboard
    lateinit var drawer: Drawer
    lateinit var viewModel: SpellingViewModel
    lateinit var badgeStyle: BadgeStyle
    var mainPages = mutableListOf<Page>()
    var closeApp: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> finish() }

    class Page(var key: MainPage, var view: View)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Kotpref.init(this)
        requestWindowFeature(FEATURE_NO_TITLE)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        window.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContentView(activity_spelling)
        setSupportActionBar(myToolbar)
        initUi()
        viewModel = ViewModelProviders.of(this).get(SpellingViewModel::class.java)
        viewModel.landingGradeTitle.observe(this, Observer<String> { it?.let { it1 -> setGrade(it1) } })
        viewModel.landingWeekTitle.observe(this, Observer<String> { it?.let { it1 -> txtWeek.text = it1 } })
        viewModel.appTitle.observe(this, Observer<String> { it?.let { it1 -> setTitle(it1) } })
        viewModel.showPage.observe(this, Observer<MainPage> { it?.let { it1 -> showPage(it1) } })
        viewModel.answerText.observe(this, Observer<String> { it?.let { it1 -> swtAnswer!!.setCurrentText(it1) } })
        viewModel.showCheckMark.observe(this, Observer<Boolean> { it?.let { it1 -> imgCheck!!.visibility = if (it1) VISIBLE else GONE } })
        viewModel.captureTyping.observe(this, Observer<Boolean> { it?.let { it1 -> if (it1) captureTyping() else stopCapturingTyping() } })
        viewModel.completionProgress.observe(this, Observer<Int> { it?.let { it1 -> prgListProgress!!.progress = it1.toFloat() } })
        viewModel.showPopup.observe(this, Observer<BasicDialogDetails> { it?.let { it1 -> showPopup(it1) } })
        viewModel.shiftKeyboard.observe(this, Observer<Boolean> { it?.let { it1 -> shiftKeyboard(it1) } })
        viewModel.changeDrawerData.observe(this, Observer<List<String>> { it?.let { it1 -> changeDrawerData(it1) } })
        viewModel.selectWeek.observe(this, Observer<List<String>> { it?.let { it1 -> launchWeekChanger(it1) } })
        viewModel.selectGrade.observe(this, Observer<List<String>> { it?.let { it1 -> launchGradeChanger(it1) } })
        mainPages.addAll(listOf(Page(MainPage.ABOUT, layoutAbout), Page(MainPage.LANDING, layoutLanding), Page(MainPage.TEST, layoutSpellingTest)))
        setBackgroundImageOnTheAboutPage()
    }

    private fun setTitle(it1: String) {
        title = it1
    }

    fun setBackgroundImageOnTheAboutPage() {
        GlideApp.with(this).load(R.drawable.skipstone).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
                layoutAboutBackground.background = resource
            }
        })
    }

    fun showPage(mainPage: MainPage) {

        for (page in mainPages) {
            page.view.visibility = if (page.key == mainPage) View.VISIBLE else View.GONE
        }
        if (MainPage.ABOUT == mainPage) {
            title = "About"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        else
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    fun setGrade(grade: String) {
        txtGrade.text = grade
        prgLoading.visibility = GONE
        txtGrade.visibility = VISIBLE
        txtWeek.visibility = VISIBLE
        btnChangeGrade.visibility = VISIBLE
        btnChangeWeek.visibility = VISIBLE
        btnStart.visibility = VISIBLE
        txtPracticeLabel.text = "Practice Makes Perfect"
    }

    fun showPopup(details: BasicDialogDetails) = showPopUp(details.title, details.text, details)
    fun changeWeek(view: View) = viewModel.launchWeekChanger()
    fun changeGrade(view: View) = viewModel.launchGradeChanger()
    fun start(view: View) = viewModel.go()

    fun drawer(text: String, badge: String) = drawer(text).withBadge(badge)!!
    fun drawer(text: String) = PrimaryDrawerItem().withName(text)!!

    fun initUi() {
        badgeStyle = BadgeStyle().withColorRes(R.color.primaryColor).withTextColorRes(R.color.primaryTextColor)
        imgCheck!!.icon!!.icon(GoogleMaterial.Icon.gmd_check)
        initTheKeyboard()
        waitForTheProgramToLoad()
        textSwitchStuff()
    }

    fun changeDrawerData(items: List<String>) {
        drawer = DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(150)
                .withToolbar(myToolbar)
                .withHeader(R.layout.my_material_drawer)
                .withSelectedItemByPosition(2)
                .addDrawerItems(drawer("Start Over"), drawer("Grade", items[0]), drawer("Week", items[1]), drawer("About"))
                .withOnDrawerItemClickListener { _, position, _ ->
                    when (position) {
                        1 -> viewModel.goToLandingPage()
                        2 -> viewModel.launchGradeChanger()
                        3 -> viewModel.launchWeekChanger()
                        4 -> viewModel.launchAboutPage()
                    }
                    false
                }.build()
    }

    fun initTheKeyboard() {
        keyboard = Keyboard(this, R.xml.my_keyboard)
        keyboardView!!.keyboard = keyboard
        keyboardView!!.isPreviewEnabled = false
        captureTyping()
    }

    fun captureTyping() = keyboardView!!.setOnKeyboardActionListener(MyKeyPressListener())

    fun stopCapturingTyping() {
        keyboardView!!.setOnKeyboardActionListener(object : ScottsKeyPressListener() {
            override fun onPress(primaryCode: Int) {}/* yup, do nothing*/
        })
    }

    fun shiftKeyboard(shifted: Boolean) {
        keyboard.isShifted = shifted
        keyboardView!!.invalidateAllKeys()
    }

    fun waitForTheProgramToLoad() {
    }

    fun launchWeekChanger(weeks: List<String>) {
        MaterialDialog.Builder(this)
                .title("Change Week")
                .items(weeks)
                .itemsCallback { _, _, _, text -> viewModel.setWeek(text.toString()) }
                .positiveText("Cancel")
                .cancelable(true)
                .onPositive { _, _ -> }
                .show()
    }

    fun launchGradeChanger(grades: List<String>) {
        MaterialDialog.Builder(this)
                .title("Change Grade")
                .items(grades)
                .itemsCallback { _, _, _, text -> viewModel.setGrade(text.toString()) }
                .positiveText("Cancel")
                .cancelable(true)
                .onPositive { _, _ -> }
                .show()
    }

    fun showPopUp(title: String, message: String, listener: DialogInterface.OnClickListener) {
        val dialog = AlertDialog.Builder(this, MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("GO", listener).show()
        val view = dialog.findViewById<View>(android.R.id.message) as TextView?

        if (view != null) {
            view.textSize = 40f
            view.gravity = CENTER
        }
    }

    fun popUpYouFinished() {
        MaterialDialog.Builder(this)
                .title("Finished!")
                .positiveText("Try Again")
                .onPositive { _, _ -> viewModel.startOver() }
                .negativeText("Exit")
                .onNegative { _, _ -> finish() }
                .cancelable(false)
                .show()
    }

    fun textSwitchStuff() {
        swtAnswer!!.setFactory {
            val myText = TextView(this@MainActivity)
            myText.gravity = CENTER
            myText.textSize = 50f
            myText.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
            myText.setTextIsSelectable(false)
            myText
        }
        swtAnswer!!.inAnimation = loadAnimation(this, slide_in_left)
        swtAnswer!!.outAnimation = loadAnimation(this, slide_out_right)
    }

    fun sendEmail(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, "info.skipstone@gmail.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from Spelling App")
        startActivity(intent)
    }

    fun launchWebsite(view: View) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://skipstone.net")))
    }

    inner class MyKeyPressListener : ScottsKeyPressListener() {
        override fun onPress(primaryCode: Int) {
            viewModel.keyPressed(primaryCode.toChar())
        }
    }


}
