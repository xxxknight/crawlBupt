package com.bupt.test;

import com.bupt.dao.NoteDao;
import com.bupt.pojo.Note;

public class testDao {
	public static void main(String[] args) {
		NoteDao noteDao = new NoteDao();
		Note note = new Note();
		note.setNoteId(1);
		note.setAuthor("123");
		noteDao.insertNote(note);
	}

}
