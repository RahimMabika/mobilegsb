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
    echo json_encode(["status" => 500, "message" => "Erreur connexion BDD"]);
    exit();
}

if (!isset($_POST["email"]) || !isset($_POST["password"])) {
    echo json_encode(["status" => 400, "message" => "Champs requis manquants"]);
    exit();
}

$email = $_POST["email"];
$password = $_POST["password"];

$sql = $pdo->prepare("SELECT id, email, password_hash, role FROM users WHERE email = :email");
$sql->execute(['email' => $email]);

if ($sql->rowCount() > 0) {
    $user = $sql->fetch(PDO::FETCH_ASSOC);

    if (password_verify($password, $user["password_hash"])) {
        echo json_encode([
            "status" => 200,
            "message" => "Connexion réussie",
            "id" => $user["id"],
            "email" => $user["email"],
            "role" => $user["role"]
        ]);
    } else {
        echo json_encode(["status" => 401, "message" => "Mot de passe incorrect"]);
    }
} else {
    echo json_encode(["status" => 404, "message" => "Utilisateur introuvable"]);
}
