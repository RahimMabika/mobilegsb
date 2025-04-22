<?php
header("Content-Type: application/json");

$host = "localhost";
$dbname = "frais_app";
$dbuser = "root";
$dbpassword = "";

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $dbuser, $dbpassword);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo json_encode(["status" => 500, "message" => "Erreur BDD"]);
    exit();
}

if (!isset($_POST["user_id"], $_POST["date"], $_POST["montant"])) {
    echo json_encode(["status" => 400, "message" => "Champs manquants"]);
    exit();
}

$user_id = $_POST["user_id"];
$date = $_POST["date"];
$montant = $_POST["montant"];

$stmt = $pdo->prepare("INSERT INTO fiches_frais (user_id, date, montant, statut) VALUES (:user_id, :date, :montant, 'en attente')");
$stmt->execute([
    "user_id" => $user_id,
    "date" => $date,
    "montant" => $montant
]);

echo json_encode(["status" => 200, "message" => "Fiche ajoutée"]);
