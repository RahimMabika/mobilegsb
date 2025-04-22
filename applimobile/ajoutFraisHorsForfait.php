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

if (!isset($_POST["fiche_id"], $_POST["categorie"], $_POST["montant"], $_POST["date"])) {
    echo json_encode(["status" => 400, "message" => "Champs manquants"]);
    exit();
}

$fiche_id = $_POST["fiche_id"];
$categorie = $_POST["categorie"];
$montant = $_POST["montant"];
$date = $_POST["date"];

$stmt = $pdo->prepare("
    INSERT INTO details_frais (fiche_id, categorie, montant, type, date)
    VALUES (:fiche_id, :categorie, :montant, 'hors forfait', :date)
");

$stmt->execute([
    "fiche_id" => $fiche_id,
    "categorie" => $categorie,
    "montant" => $montant,
    "date" => $date
]);

echo json_encode(["status" => 200, "message" => "Frais hors forfait ajouté"]);
