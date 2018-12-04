package mk.android.com.canvasdrawview.presenter

import mk.android.com.canvasdrawview.interactor.ShapesInteractor
import mk.android.com.canvasdrawview.model.Shape
import java.io.Serializable

/**
 * Created by Mayuri Khinvasara on 04,December,2018
 */
/*
    Bind Stats activity with interactor
 */
class StatsPresenter {

    val countByGroup: Serializable
        get() = ShapesInteractor.getInstance().countByGroup

    fun deleteAllByShape(shapeType: Shape.Type) {
        ShapesInteractor.getInstance().deleteAllByShape(shapeType)
    }
}
