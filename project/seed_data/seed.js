import { faker } from "@faker-js/faker";
import admin from "firebase-admin";
import { getFirestore, FieldValue } from "firebase-admin/firestore";
import { getAuth } from "firebase-admin/auth";

// Este seed es solo para los emuladores locales; fija IPv4 para coincidir
// con el host 127.0.0.1 que usa Firebase CLI en Windows.
process.env.FIRESTORE_EMULATOR_HOST = "127.0.0.1:8080";
process.env.FIREBASE_AUTH_EMULATOR_HOST = "127.0.0.1:9099";

try {
  admin.initializeApp({ projectId: "kloth-7e866" });
  //credential: admin.credential.applicationDefault(),
} catch (e) {
  // ya inicializado
}

const db = getFirestore();
const auth = getAuth();

const ARTICLE_TYPES = ["PRENDA", "OUTFIT", "MARCA", "EVENTO"];

function randomItem(arr) {
  return arr[Math.floor(Math.random() * arr.length)];
}

function pastIso(days = 90) {
  const d = faker.date.recent({ days });
  return d.toISOString();
}

async function clearCollection(path) {
  const snap = await db.collection(path).get();
  if (snap.empty) {
    console.log(`[CLEAR] ${path} ya está vacía`);
    return;
  }
  let batch = db.batch();
  let count = 0;
  for (const doc of snap.docs) {
    batch.delete(doc.ref);
    count++;
    if (count % 400 === 0) {
      await batch.commit();
      batch = db.batch();
    }
  }
  if (count % 400 !== 0) {
    await batch.commit();
  }
  console.log(`[CLEAR] Eliminados ${count} documentos de ${path}`);
}

async function clearAuthUsers() {
  try {
    const listUsers = await auth.listUsers(100);
    for (const u of listUsers.users) {
      await auth.deleteUser(u.uid);
    }
    console.log(`[CLEAR] Eliminados ${listUsers.users.length} usuarios de Firebase Auth Emulator`);
  } catch (e) {
    console.log("[CLEAR] No se pudieron limpiar usuarios de Auth:", e.message);
  }
}

async function seedFollowRelationships(users) {
  if (users.length < 2) {
    console.log("[FOLLOW] Se necesitan al menos 2 usuarios; no se agregaron relaciones.");
    return;
  }

  const followingCounts = new Map(users.map((user) => [user.userId, 0]));
  const followersCounts = new Map(users.map((user) => [user.userId, 0]));
  const relationshipsPerUser = Math.min(2, users.length - 1);
  const batch = db.batch();

  // Cada relación A -> B vive en las dos rutas consultadas por la app:
  // users/A/following/B y users/B/followers/A.
  for (let followerIndex = 0; followerIndex < users.length; followerIndex++) {
    const follower = users[followerIndex];

    for (let offset = 1; offset <= relationshipsPerUser; offset++) {
      const target = users[(followerIndex + offset) % users.length];
      const timestamp = FieldValue.serverTimestamp();
      const followingRef = db.collection("users").doc(follower.userId)
        .collection("following").doc(target.userId);
      const followersRef = db.collection("users").doc(target.userId)
        .collection("followers").doc(follower.userId);

      batch.set(followingRef, { timestamp });
      batch.set(followersRef, { timestamp });
      followingCounts.set(follower.userId, followingCounts.get(follower.userId) + 1);
      followersCounts.set(target.userId, followersCounts.get(target.userId) + 1);
    }
  }

  // Mantener los contadores del documento raíz sincronizados con las subcolecciones.
  for (const user of users) {
    batch.update(db.collection("users").doc(user.userId), {
      followingCount: followingCounts.get(user.userId),
      followersCount: followersCounts.get(user.userId),
    });
  }

  await batch.commit();
  console.log(
    `[FOLLOW] Creadas ${users.length * relationshipsPerUser} relaciones: ` +
    `${relationshipsPerUser} seguidos y ${relationshipsPerUser} seguidores por usuario.`
  );
}

function buildUserObject(userId, firstName, lastName) {
  const fullName = `${firstName} ${lastName}`;
  const username = faker.internet.username({ firstName, lastName }).toLowerCase();
  const email = faker.internet.email({ firstName, lastName }).toLowerCase();
  const imageURL = faker.image.avatar();

  return {
    userId,
    email,
    password: "password123",
    username,
    fullName,
    imageURL,
    biography: faker.lorem.sentence({ min: 4, max: 10 }),
    location: faker.location.city(),
    website: faker.internet.url(),
    registeredAt: pastIso(365),
    eliminated: false,
    eliminatedAt: null,
    userType: "USER",
    createdAt: pastIso(365),
    updatedAt: pastIso(30),
    followersCount: 0,
    followingCount: 0,
  };
}

function buildArticle(userId, typeArticle) {
  const base = {
    typeArticle,
    title: faker.commerce.productName(),
    description: faker.lorem.paragraph({ min: 2, max: 4 }),
    idUser: userId,
    imageArticle: faker.image.urlPicsumPhotos({ width: 1080, height: 1350 }),
    createdAt: pastIso(60),
    eliminated: false,
    updatedAt: null,
    tags: faker.helpers.arrayElements(
      ["streetwear", "vintage", "minimal", "y2k", "casual", "formal", "sport", "denim", "oversize", "grunge"],
      { min: 2, max: 4 }
    ),
  };

  if (typeArticle === "PRENDA") {
    return {
      ...base,
      size: randomItem(["XS", "S", "M", "L", "XL", "XXL"]),
      color: faker.color.human(),
      category: randomItem(["camiseta", "pantalón", "chaqueta", "zapatos", "vestido", "falda", "jersey"]),
      material: randomItem(["algodón", "poliéster", "lana", "denim", "cuero", "seda"]),
      brand: faker.company.name(),
      price: parseFloat(faker.commerce.price({ min: 15, max: 250, dec: 2 })),
      style: null,
      website: null,
      originCountry: null,
      foundedAt: null,
      city: null,
      country: null,
      startDate: null,
      endDate: null,
      organizer: null,
    };
  }

  if (typeArticle === "OUTFIT") {
    return {
      ...base,
      size: null,
      color: null,
      category: null,
      material: null,
      brand: null,
      price: null,
      style: randomItem(["urbano", "casual", "formal", "deportivo", "bohemio", "minimalista"]),
      website: null,
      originCountry: null,
      foundedAt: null,
      city: null,
      country: null,
      startDate: null,
      endDate: null,
      organizer: null,
    };
  }

  if (typeArticle === "MARCA") {
    return {
      ...base,
      size: null,
      color: null,
      category: null,
      material: null,
      brand: faker.company.name(),
      price: null,
      style: null,
      website: faker.internet.url(),
      originCountry: faker.location.country(),
      foundedAt: faker.number.int({ min: 1970, max: 2018 }),
      city: null,
      country: null,
      startDate: null,
      endDate: null,
      organizer: null,
    };
  }

  if (typeArticle === "EVENTO") {
    const start = faker.date.soon({ days: 30 });
    const end = new Date(start.getTime() + faker.number.int({ min: 2, max: 72 }) * 60 * 60 * 1000);
    return {
      ...base,
      size: null,
      color: null,
      category: null,
      material: null,
      brand: null,
      price: null,
      style: null,
      website: faker.internet.url(),
      originCountry: null,
      foundedAt: null,
      city: faker.location.city(),
      country: faker.location.country(),
      startDate: start.toISOString(),
      endDate: end.toISOString(),
      organizer: faker.company.name(),
    };
  }

  return base;
}

async function main() {
  try {
    console.log("=== KLoth Seed Data ===");
    console.log("Project: kloth-7e866");
    console.log("Firestore Emulator:", process.env.FIRESTORE_EMULATOR_HOST);
    console.log("Auth Emulator:", process.env.FIREBASE_AUTH_EMULATOR_HOST);
    console.log("Proceso: Limpieza -> 5 Usuarios (Auth + Firestore) -> Seguidos/seguidores -> Posts -> Reviews -> Comentarios -> Likes\n");

    // 0. Limpiar datos previos
    await clearCollection("users");
    await clearCollection("articles");
    await clearCollection("reviews");
    await clearAuthUsers();

    // 1. Crear 5 usuarios en Firebase Auth Emulator Y en Firestore
    const users = [];
    for (let i = 0; i < 5; i++) {
      const firstName = faker.person.firstName();
      const lastName = faker.person.lastName();
      const userId = faker.string.uuid();

      const userObj = buildUserObject(userId, firstName, lastName);

      // Crear en Firebase Auth
      await auth.createUser({
        uid: userObj.userId,
        email: userObj.email,
        password: userObj.password,
        displayName: userObj.fullName,
        photoURL: userObj.imageURL,
      });

      // Crear en Firestore
      await db.collection("users").doc(userObj.userId).set(userObj);

      users.push(userObj);
      console.log(`[USER] Creado ${userObj.username} (${userObj.email} / pass: ${userObj.password}) | ID: ${userObj.userId}`);
    }

    // 2. Crear las relaciones recíprocas y actualizar los contadores de usuarios.
    await seedFollowRelationships(users);

    // 3. Asignar 2 artículos/posts a cada usuario
    const articleRefs = [];
    for (const user of users) {
      for (let p = 0; p < 2; p++) {
        const typeArticle = randomItem(ARTICLE_TYPES);
        const article = buildArticle(user.userId, typeArticle);
        const docRef = await db.collection("articles").add(article);
        articleRefs.push({ id: docRef.id, authorId: user.userId, title: article.title });
        console.log(`[ARTICLE] ${typeArticle} ("${article.title}") -> ID: ${docRef.id} | Autor: ${user.username}`);
      }
    }

    // 4. Asignar a cada post sus reseñas
    const reviewRefs = [];
    for (const article of articleRefs) {
      const numReviews = faker.number.int({ min: 1, max: 3 });
      for (let r = 0; r < numReviews; r++) {
        const reviewer = randomItem(users);
        const reviewIdRef = db.collection("reviews").doc();
        const reviewData = {
          reviewId: reviewIdRef.id,
          userId: reviewer.userId,
          articleId: article.id,
          rating: faker.number.int({ min: 3, max: 5 }),
          text: faker.lorem.sentence({ min: 6, max: 12 }),
          likesCount: 0,
          commentsCount: 0,
          createdAt: pastIso(45),
          updatedAt: null,
          eliminated: false,
        };
        await reviewIdRef.set(reviewData);
        reviewRefs.push({ id: reviewIdRef.id, articleId: article.id });
        console.log(`[REVIEW] ${reviewData.rating}★ -> ID: ${reviewIdRef.id} en Post "${article.title}" | Por: ${reviewer.username}`);
      }
    }

    // 5. Asignar comentarios a las reseñas y 6. Asignar likes a las reseñas
    for (const review of reviewRefs) {
      // Comentarios
      const numComments = faker.number.int({ min: 1, max: 3 });
      for (let c = 0; c < numComments; c++) {
        const commenter = randomItem(users);
        const commentRef = db.collection("reviews").doc(review.id).collection("comments").doc();
        const maxCommentLikes = Math.min(users.length, 3);
        const commentLikers = faker.helpers.arrayElements(users, {
          min: 1,
          max: maxCommentLikes,
        });
        const commentData = {
          commentId: commentRef.id,
          userId: commenter.userId,
          username: commenter.username,
          profileImageUrl: commenter.imageURL || null,
          text: faker.lorem.sentence({ min: 4, max: 8 }),
          createdAt: pastIso(20),
          likesCount: commentLikers.length,
        };
        await commentRef.set(commentData);
        for (const liker of commentLikers) {
          await commentRef.collection("likes").doc(liker.userId).set({
            timestamp: FieldValue.serverTimestamp(),
          });
        }
        console.log(`  [COMMENT] "${commentData.text}" -> Por: ${commenter.username}`);
        console.log(`  [COMMENT LIKES] ${commentLikers.length} likes -> ${commentRef.id}`);
      }

      // Actualizar número de comentarios en la reseña
      const commentsSnap = await db.collection("reviews").doc(review.id).collection("comments").get();
      await db.collection("reviews").doc(review.id).update({ commentsCount: commentsSnap.size });

      // Likes a la reseña
      const numLikes = faker.number.int({ min: 1, max: 4 });
      const likers = faker.helpers.arrayElements(users, { min: 1, max: numLikes });
      for (const liker of likers) {
        await db.collection("reviews").doc(review.id).collection("likes").doc(liker.userId).set({
          timestamp: FieldValue.serverTimestamp(),
        });
      }
      await db.collection("reviews").doc(review.id).update({ likesCount: likers.length });
      console.log(`  [LIKES] ${likers.length} likes agregados a reseña ${review.id}`);
    }

    console.log("\n=== SEED COMPLETADO CON ÉXITO ===");
    console.log(`- Usuarios creados (Auth + Firestore): ${users.length}`);
    console.log(`- Artículos creados: ${articleRefs.length}`);
    console.log(`- Reseñas creadas: ${reviewRefs.length}`);
    console.log("Puedes usar el email/password (password123) de cualquiera de los usuarios impresos para iniciar sesión en la app.\n");
    process.exit(0);
  } catch (err) {
    console.error("ERROR EN SEED:", err);
    process.exit(1);
  }
}

main();
