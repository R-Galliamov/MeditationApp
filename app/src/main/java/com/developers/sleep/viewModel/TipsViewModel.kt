import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developers.sleep.R
import com.developers.sleep.dataModels.Tip
import com.developers.sleep.repository.TipsRepository
import java.util.Calendar
import javax.inject.Inject

class TipsViewModel @Inject constructor(
    private val application: Application,
) : AndroidViewModel(application) {

    private val repository = TipsRepository()

    val tipsList = repository.getTipsFromAssets(application)

    private val _tipOfTheDay = MutableLiveData<Tip>()
    val tipOfTheDay: LiveData<Tip>
        get() = _tipOfTheDay

    private val _currentTip = MutableLiveData<Tip>()
    val currentTip: LiveData<Tip>
        get() = _currentTip

    init {
        setDrawableResourceForPanel()
        setImageResource()
        updateTipOfTheDay()
    }

    fun setCurrentTip(tip: Tip) {
        _currentTip.value = tip
    }

    private fun updateTipOfTheDay() {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val tipIndex = (dayOfYear - 1) % tipsList.size
        val selectedTip = tipsList[tipIndex]
        val tip =
            Tip(selectedTip.name, selectedTip.content, selectedTip.panelRes, selectedTip.imageRes)
        _tipOfTheDay.value = tip
    }

    private fun setDrawableResourceForPanel() {
        for (i in tipsList.indices) {
            tipsList[i].panelRes = getPanelDrawableResource(i)
        }
    }

    private fun getPanelDrawableResource(listIndex: Int): Int {
        val index = listIndex % 4
        return when (index) {
            0 -> R.drawable.rectangle_half_green
            1 -> R.drawable.rectangle_half_blue
            2 -> R.drawable.rectangle_half_yellow
            3 -> R.drawable.rectangle_half_crimson
            else -> R.drawable.rectangle_half_green
        }
    }


    private fun setImageResource() {
        for (i in tipsList.indices) {
            tipsList[i].imageRes = getImageResource(i)
        }
    }

    private fun getImageResource(listIndex: Int): Int {
        val index = listIndex % 5
        return when (index) {
            0 -> R.drawable.tip_pic1
            1 -> R.drawable.tip_pic2
            2 -> R.drawable.tip_pic3
            3 -> R.drawable.tip_pic4
            4 -> R.drawable.tip_pic5
            else -> R.drawable.tip_pic1
        }
    }
}
