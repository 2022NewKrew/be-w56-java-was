package view;

import model.Model;

public class ViewCreator {

    public static void userListView(Model model, String path) {
        Object userList = model.getAttribute("userList");
        String thead = TableComponentFrame.thead("#", "아이디", "이메일", "이름", " ");
        String tbody = TableComponentFrame.tbody(userList, "userId", "email", "name");
        String userTable = TableFrame.create(thead + tbody);
        String userListView = ViewFrame.createView(userTable);
        ViewMapper.addView(path, userListView);
    }

}
