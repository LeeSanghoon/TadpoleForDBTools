/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object.rdb;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.mysql.TriggerDAO;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.editors.objects.table.GetDDLTableSource;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * generate ddl view     
 * 
 * @author hangum
 *
 */
public class GenerateViewDDLAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateViewDDLAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateViewDDLAction"; //$NON-NLS-1$
	
	/**
	 * generated view ddl
	 * 
	 * @param window
	 * @param actionType
	 * @param target
	 */
	public GenerateViewDDLAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String target) {
		super(window, actionType);
	
		setId(ID + actionType.toString());
		setText(target + " DDL" );
		window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void run() {
		try {
			
			if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {
				TableDAO tableDAO = (TableDAO)sel.getFirstElement();
				FindEditorAndWriteQueryUtil.run(userDB, GetDDLTableSource.getSource(userDB, actionType, tableDAO.getName()) + PublicTadpoleDefine.SQL_DILIMITER);				
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
				
				FindEditorAndWriteQueryUtil.run(userDB, GetDDLTableSource.getSource(userDB, actionType, (String)sel.getFirstElement()) + PublicTadpoleDefine.SQL_DILIMITER);
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
				InformationSchemaDAO indexDAO = (InformationSchemaDAO)sel.getFirstElement();
				
				FindEditorAndWriteQueryUtil.run(userDB, GetDDLTableSource.getIndexSource(userDB, actionType, indexDAO.getINDEX_NAME(), indexDAO.getTABLE_NAME()) + PublicTadpoleDefine.SQL_DILIMITER);
			} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
				TriggerDAO triggerDAO = (TriggerDAO)sel.getFirstElement();
				
				FindEditorAndWriteQueryUtil.run(userDB, triggerDAO.getStatement() + PublicTadpoleDefine.SQL_DILIMITER);
			}
			
		} catch(Exception ee) {
			MessageDialog.openError(null, "Confirm", "Not support this function.");
		}
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
			
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
//			if(userDB != null) {
				if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
					setEnabled(this.sel.size() > 0);
				} else setEnabled(false);
//			}
//		} else {
//			setEnabled(false);
		}

	}
	
}