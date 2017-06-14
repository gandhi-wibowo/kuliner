<?php
require APPPATH . '/libraries/REST_Controller.php';
class Kuliner extends REST_Controller {

  function __construct($config = 'rest') {
      parent::__construct($config);
  }

  function index_get(){ // get / read
    $cari = $this->get("keyword"); // cari kuliner berdasarkan keyword
    $id   = $this->get("id_kuliner"); // get one kuliner by id_kuliner
    $idMenu = $this->get("Mid_kuliner"); // get menu by id_kuliner
    $getKuliner = $this->get("id_user");// get one kuliner by id_user
    $getMenu = $this->get("id_menu");// get one menu
    if($getMenu != NULL){
      $this->db->where('id_menu',$getMenu);
      $menu = $this->db->get("menu");
      if($menu->num_rows()>0){
        $kuliner = $menu->result();
      }
      else{
        $kuliner = array(array("id_menu"=>"fail"));
      }
    }
    else if($getKuliner != NULL){
      $this->db->where('id_user',$getKuliner);
      $kuli = $this->db->get("kuliner");
      if($kuli->num_rows()>0){
        $kuliner = $kuli->result();
      }
      else{
        $kuliner = array(array("id_kuliner"=>"fail"));
      }
    }
    else if($cari != NULL){
      $this->db->select("kuliner.id_kuliner,nama_kuliner,alamat_kuliner,kategori_kuliner,latitude,longitude,gambar_kuliner");
      $this->db->join("menu","menu.id_kuliner = kuliner.id_kuliner");
      $this->db->like("nama_menu",$cari);
      $this->db->or_like("nama_kuliner",$cari);
      $this->db->or_like("alamat_kuliner",$cari);
      $this->db->or_like("kategori_kuliner",$cari);
      $this->db->group_by("kuliner.nama_kuliner");
      $kuliner = $this->db->get("kuliner")->result();
    }
    else if($id != NULL){
      $this->db->select("kuliner.id_kuliner,nama_kuliner,alamat_kuliner,kategori_kuliner,latitude,longitude,gambar_kuliner");
      $this->db->where('kuliner.id_kuliner',$id);
      $this->db->join("menu","menu.id_kuliner = kuliner.id_kuliner");
      $this->db->group_by("kuliner.nama_kuliner");
      $kuliner = $this->db->get('kuliner')->result();
    }
    else if($idMenu != NULL){
      $this->db->where("id_kuliner",$idMenu);
      $kuliner = $this->db->get("menu")->result();
    }
    else{
      $this->db->select("kuliner.id_kuliner,nama_kuliner,alamat_kuliner,kategori_kuliner,latitude,longitude,gambar_kuliner");
      $this->db->order_by('kuliner.id_user', 'RANDOM');
      $this->db->join("menu","menu.id_kuliner = kuliner.id_kuliner");
      $this->db->group_by("kuliner.nama_kuliner");
      $kuliner = $this->db->get("kuliner",10)->result();
    }
    $this->response($kuliner,200);
  }

  function index_post(){ // post / insert
    $idUser = $this->post("id_user");
    $idKuliner = $this->post("id_kuliner");
    $new = $this->post("new");
    $upd = $this->post("upd");
    $ll = $this->post("latLong");

    if($ll != NULL){
      $data = array(
        'latitude'=>$this->post("latitude"),
        'longitude'=>$this->post("longitude")
      );
      $this->db->where("id_kuliner",$ll);
      $update = $this->db->update('kuliner', $data);
      if ($update) {
          $this->response(array($data), 200);
      } else {
        $data = array('latitude'=>"fail");
        $this->response(array($data),200);
      }
    }
    else if($new != NULL){
      $name = uplodGambar();
      if( $name != FALSE){
        // kalo gambar udah di upload
        $data = array(
          'id_user'         => $idUser,
          'nama_kuliner'    => $this->post("nama_kuliner"),
          'alamat_kuliner'  => $this->post("alamat_kuliner"),
          'kategori_kuliner'=> $this->post("kategori_kuliner"),
          'gambar_kuliner'  => $name
        );
        $insert = $this->db->insert("kuliner",$data);
        if ($insert) {
            $this->response(array($data), 200);
        } else {
            $this->response(array(array('id_user'=>"fail")),200);
        }
      }
      else{
        $data = array(
          'id_user'         => $idUser,
          'nama_kuliner'    => $this->post("nama_kuliner"),
          'alamat_kuliner'  => $this->post("alamat_kuliner"),
          'kategori_kuliner'=> $this->post("kategori_kuliner")
        );
        $insert = $this->db->insert("kuliner",$data);
        if ($insert) {
            $this->response(array($data), 200);
        } else {
            $this->response(array(array('id_user'=>"fail")),200);
        }
      }

    }
    else if($upd != NULL){
      $name = uplodGambar();
      if($name != FALSE){
        $data = array(
          'nama_kuliner'    => $this->post("nama_kuliner"),
          'alamat_kuliner'  => $this->post("alamat_kuliner"),
          'kategori_kuliner'=> $this->post("kategori_kuliner"),
          'gambar_kuliner'  => $name
        );
        $this->db->where("id_kuliner",$idKuliner);
        $update = $this->db->update('kuliner', $data);
        if ($update) {
            $this->response(array($data), 200);
        } else {
          $data = array('id_user'=>"fail");
          $this->response(array($data),200);
        }
      }
      else{
        $data = array(
          'nama_kuliner'    => $this->post("nama_kuliner"),
          'alamat_kuliner'  => $this->post("alamat_kuliner"),
          'kategori_kuliner'=> $this->post("kategori_kuliner")
        );
        $this->db->where("id_kuliner",$idKuliner);
        $update = $this->db->update('kuliner', $data);
        if ($update) {
            $this->response(array($data), 200);
        } else {
          $data = array('id_user'=>"fail");
          $this->response(array($data),200);
        }
      }
    }
    else if($idKuliner != NULL){
      $data = array(
        'id_kuliner'    => $idKuliner,
        'nama_menu'     => $this->post("nama_menu"),
        'harga_menu'    => $this->post("harga_menu")
      );
      $insert = $this->db->insert("menu",$data);
      if ($insert) {
          $this->response(array($data), 200);
      } else {
        $data = array('id_user'=>"fail");
        $this->response(array($data),200);
      }
    }
  }

  function index_put(){
    $idMenu = $this->put("id_menu");
    $idDelete = $this->put("id_del");
    if($idMenu != NULL){
      $data = array(
        'nama_menu'     => $this->put("nama_menu"),
        'harga_menu'    => $this->put("harga_menu")
      );
      $this->db->where("id_menu",$idMenu);
      $update = $this->db->update('menu', $data);
      if ($update) {
        $data = array('status'=>"sukses");
          $this->response(array($data), 200);
      } else {
        $data = array('status'=>"fail");
        $this->response(array($data),200);
      }
    }
    else if($idDelete != NULL){
      $this->db->where("id_menu",$idDelete);
      $delete = $this->db->delete('menu');
      if ($delete) {
        $data = array('status'=>"sukses");
        $this->response(array($data), 200);
      } else {
        $data = array('status'=>"fail");
        $this->response(array($data),200);
      }
    }
  }
}
?>
