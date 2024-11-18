package com.noteapp.service.server;

import com.noteapp.dao.DAOException;
import com.noteapp.dao.ShareNoteDAO;
import com.noteapp.dao.ShareNoteKey;
import com.noteapp.model.dto.ShareNote;
import java.util.List;
import com.noteapp.dao.BasicDAO;

/**
 * Lấy tất cả các ShareNote được share tới user
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class GetAllReceivedNotesService implements CollectionServerService<ShareNote> {
    private String receiver;
    protected BasicDAO<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public GetAllReceivedNotesService() {
        receiver = "";
    }
    
    public GetAllReceivedNotesService(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    @Override
    public List<ShareNote> execute() throws DAOException {
        shareNoteDataAccess = ShareNoteDAO.getInstance();
        return shareNoteDataAccess.getAll(new ShareNoteKey(-1, receiver));
    }
}