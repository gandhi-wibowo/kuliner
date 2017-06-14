<?php
require APPPATH . '/libraries/REST_Controller.php';
class Login extends REST_Controller {

  function __construct($config = 'rest') {
      parent::__construct($config);
  }

  function index_get(){ // login, cek login
    $login = $this->get("login");
    $cekLogin = $this->get("cek_login");
    $ping = $this->get("ping");
    $email = $this->get("email");
    $password = $this->get("password");
    $idUser = $this->get("id_user");
    if($idUser != NULL){
      $data = array('id_user'=>$idUser);
      $this->db->where($data);
      $user = $this->db->get('user');
      if($user->num_rows()>0){
        $this->response($user->result(),200);
      }
      else{
        $data = array('id_user'=>"fail");
        $this->response(array($data),200);
      }

    }
    else if($login != NULL){
      $data = array(
        'email_user' => $email,
        'password_user' => sha1($password)
      );
      $this->db->where($data);
      $user = $this->db->get('user');
      if($user->num_rows()>0){
        $data = array();
        foreach ($user->result() as $key => $value) {
          $data['status']="sukses";
          $data['id_user']=$value->id_user;
          $data['nama_user']=$value->nama_user;
          $data['hp_user']=$value->hp_user;
          $data['password_user']=$value->password_user;
          $data['email_user']=$value->email_user;
        }
        $this->response(array($data), 200);
      }
      else{
        $data = array("status"=>"username atau password salah");
        $this->response(array($data), 200);
      }
    }
    else if($cekLogin != NULL){
      $data = array(
        'email_user' => $email,
        'password_user' => $password
      );
      $this->db->where($data);
      $user = $this->db->get('user');
      if($user->num_rows()>0){
        $data = array("status"=>"sukses");
        $this->response(array($data), 200);
      }
      else{
        $data = array("status"=>"username atau password salah");
        $this->response(array($data), 200);
      }
    }
    else if ($ping != NULL) {
      $data = array("status"=>"online");
      $this->response(array($data), 200);
    }

  }
  function index_post(){ // daftar

    if(cekValue("user","email_user",$this->post("email_user"))){
      $data = array(
        'nama_user'   => $this->post("nama_user"),
        'email_user'  => $this->post("email_user"),
        'hp_user'     => $this->post("hp_user"),
        'password_user'=> sha1($this->post("password_user"))
      );
      $insert = $this->db->insert('user', $data);
      if ($insert) {
          $this->response(array($data), 200);
      } else {
        $data = array("nama_user"=>"gagal_mendaftar");
        $this->response(array($data), 200);
      }
    }
    else{
      $data = array("nama_user"=>"email_sudah_terdaftar");
      $this->response(array($data), 200);
    }


  }
  function index_put(){ // ubah nama, ubah password
    $pwd = $this->put("pwd");
    $upd = $this->put("upd");
    $idUser = $this->put("id_user");
    $oldPassword = sha1($this->put("old_password"));
    $password = sha1($this->put("password_user"));

    if($upd != NULL){
      $data = array(
        'nama_user'   => $this->put("nama_user"),
        'email_user'  => $this->put("email_user"),
        'hp_user'     => $this->put("hp_user")
      );
      $this->db->where('id_user', $idUser);
      $update = $this->db->update('user', $data);
      if ($update) {
          $this->response(array($data), 200);
      } else {
        $data = array("nama_user"=>"gagal_update_data");
        $this->response(array($data), 200);
      }
    }
    else if($pwd != NULL){
      $cekData = array('id_user'=>$idUser,'password_user'=>$oldPassword);
      $this->db->where($cekData);
      $cek = $this->db->get('user');
      if($cek->num_rows()>0){
        // kalau password lama cocok
        $data = array(
          'password_user'   => $password
        );
        $this->db->where('id_user', $idUser);
        $update = $this->db->update('user', $data);
        if ($update) {
            $this->response(array($data), 200);
        } else {
          $data = array("password_user"=>"gagal");
          $this->response(array($data), 200);
        }

      }
      else{
        $data = array("password_user"=>"salah");
        $this->response(array($data), 200);
      }
    }
  }
}
?>
