package com.frice.designer.click

import com.frice.designer.Controller
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.event.MenuKeyEvent
import javax.swing.event.MenuKeyListener

/**
 * Created by ice1000 on 2016/9/16.
 *
 * @author ice1000
 */
class RightClickPopup(controller: Controller) {
	val popup: JPopupMenu
		get () = JPopupMenu().apply {
			add(JMenuItem("Add item").apply {
				addMenuKeyListener(object : MenuKeyListener {
					override fun menuKeyReleased(e: MenuKeyEvent?) = Unit
					override fun menuKeyTyped(e: MenuKeyEvent?) = Unit
					override fun menuKeyPressed(e: MenuKeyEvent?) {
					}
				})
			})
		}
}