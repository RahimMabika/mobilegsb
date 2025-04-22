<?php
header("Content-Type: application/json");

$host = "localhost";
$dbname = "frais_app";
$dbuser = 'root';
$dbpassword = '';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $dbuser, $dbpassword);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo json_encode(["status" => 500, "message" => "Erreur BDD"]);
    exit();
}

if (!isset($_GET['user_id'])) {
    echo json_encode(["status" => 400, "message" => "Paramètre manquant"]);
    exit();
}

$user_id = $_GET['user_id'];

$stmt = $pdo->prepare("SELECT id, date, montant, statut FROM fiches_frais WHERE user_id = :id ORDER BY date DESC");
$stmt->execute(['id' => $user_id]);

$fiches = $stmt->fetchAll(PDO::FETCH_ASSOC);

echo json_encode([
    "status" => 200,
    "fiches" => $fiches
]);
?>
