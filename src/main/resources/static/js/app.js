$(async function () {
    await getTableWithUsers();
    getDefaultModal();
    getNewUserButton();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('api/users'),
    findOneUser: async (id) => await fetch(`api/users/${id}`),
    addNewUser: async (user) => await fetch('api/users', {method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`api/updateUsers/${id}`, {method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`api/users/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let table = $('#mainTableWithUsers tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.lastName}</td>
                            <td>${user.address}</td>
                            <td>${user.roleSet.map(role => role.roleName).join(', ')}</td>  
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-sm btn-primary" 
                                data-toggle="modal" data-target="#someDefaultModal">edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-sm btn-danger" 
                                data-toggle="modal" data-target="#someDefaultModal">delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })

    // обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#mainTableWithUsers").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');
        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}
async function getNewUserButton() {

    $("#newUserButton").on('click', (event) => {
        let defaultModal = $('#someDefaultModal');
        let targetButton = $(event.target);
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}

// что то деалем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
            case 'new':
                newUser(thisModal);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();
    user.then(user => {
            let bodyForm = `
            <form class="form-group" id="editUser">
                <label for="name"><b>Name</b></label>
                <input class="form-control" type="text" id="name" value="${user.name}"><br>
                <label for="lastName"><b>Last Name</b></label>
                <input class="form-control" type="text" id="lastName" value="${user.lastName}"><br>
                <input class="form-control" type="password" id="password"><br>
                <label for="address"><b>Address</b></label>
                <input class="form-control" id="address" type="text" value="${user.address}">
                <label for="roleSet"><b>Role</b></label>
                <select class="form-control" id="roleSet" multiple name="roleSet">
                    <option name="roleSet" value="1">Admin</option>
                    <option name="roleSet" value="2">User</option>
                </select>
            </form>
        `;
            modal.find('.modal-body').append(bodyForm);
        })

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    $("#editButton").on('click', async () => {
        let name = modal.find("#name").val().trim();
        let lastName = modal.find("#lastName").val().trim();
        let password = modal.find("#password").val().trim();
        let address = modal.find("#address").val().trim();
        let roleSet = modal.find("#roleSet").val();
        let data = {
            name: name,
            lastName: lastName,
            password: password ?? '',
            address: address,
            roleSet: roleSet
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}


// удаляем юзера из модалки удаления
async function deleteUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Delete User');
    let deleteButton = `<button  class="btn btn-outline-danger" id="deleteButton">Delete</button>`; // написать выполнение как в эдит
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                <label for="name"><b>Name</b></label>
                <input class="form-control" type="text" id="name" value="${user.name}"><br>
                <label for="lastName"><b>Last Name</b></label>
                <input class="form-control" type="text" id="lastName" value="${user.lastName}"><br>
                <label for="address"><b>Address</b></label>
                <input class="form-control" id="address" type="text" value="${user.address}">
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })
    $("#deleteButton").on('click', async () => {
        const response = await userFetchService.deleteUser(id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
    getTableWithUsers();
    modal.modal('hide');

}


async function newUser(modal) {
        let bodyForm = `
            <form class="form-group" id="editUser">
                <label for="name"><b>Name</b></label>
                <input class="form-control" type="text" id="name" placeholder="name"><br>
                <label for="lastName"><b>Last Name</b></label>
                <input class="form-control" type="text" id="lastName" placeholder="lastName"><br>
                <label for="password"><b>password</b></label>
                <input class="form-control" type="password" id="password"><br>
                <label for="address"><b>Address</b></label>
                <input class="form-control" id="address" type="text" ">
                <label for="roleSet"><b>Role</b></label>
                <select class="form-control" id="roleSet" multiple name="roleSet">
                    <option name="roleSet" value="1">Admin</option>
                    <option name="roleSet" value="2">User</option>
                </select>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);

    modal.find('.modal-title').html('Create User');

    let createButton = `<button  class="btn btn-outline-success" id="createUserButton">Create User</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(createButton);
    modal.find('.modal-footer').append(closeButton);

    $("#createUserButton").on('click', async () => {
        let name = modal.find("#name").val().trim();
        let lastName = modal.find("#lastName").val().trim();
        let password = modal.find("#password").val().trim();
        let address = modal.find("#address").val().trim();
        let roleSet = modal.find("#roleSet").val();
        let data = {
            name: name,
            lastName: lastName,
            password: password,
            address: address,
            roleSet: roleSet
        }
        const response = await userFetchService.addNewUser(data);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}