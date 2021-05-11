package com.sportphotos.infrastructure.database.init;

import com.github.javafaker.Faker;
import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.Location;
import com.sportphotos.domain.events.model.PhotoCoverage;
import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.domain.photographers.model.Rating;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Fakes {

  private static final Faker faker = new Faker();
  private static int nicknameCounter = 10000;
  private static int eventNameCounter = 10000;

  static Event fakeEvent(Supplier<List<PhotoCoverage>> photoCoverages) {
    return Event.builder()
        .id(UUID.randomUUID().toString())
        .date(
            faker
                .date()
                .past(1000, TimeUnit.DAYS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate())
        .name(faker.lordOfTheRings().location() + " - " + eventNameCounter++)
        .photoCoverages(photoCoverages.get())
        .avatar(new Binary(BsonBinarySubType.BINARY, faker.avatar().image().getBytes()))
        .location(new Location(faker.starTrek().location(), faker.country().name()))
        .build();
  }

  static List<Event> fakeEvents(
      int count, int photoCoveragesCount, List<Photographer> photographers) {

    return fakeCollection(
        count, () -> fakeEvent(() -> fakePhotoCoverages(photoCoveragesCount, photographers)));
  }

  static PhotoCoverage fakePhotoCoverage(Photographer photographer) {
    return PhotoCoverage.builder()
        .id(UUID.randomUUID().toString())
        .description(faker.chuckNorris().fact())
        .link(faker.internet().url())
        .bestPhoto(new Binary(BsonBinarySubType.BINARY, faker.internet().image().getBytes()))
        .photographer(photographer)
        .build();
  }

  static List<PhotoCoverage> fakePhotoCoverages(int count, List<Photographer> photographers) {
    return fakeCollection(count, () -> fakePhotoCoverage(choose(photographers)));
  }

  static Photographer fakePhotographer(Supplier<List<Rating>> ratings) {
    return Photographer.builder()
        .id(UUID.randomUUID().toString())
        .nickname(faker.gameOfThrones().character() + " - " + nicknameCounter++)
        .ratings(ratings.get())
        .build();
  }

  static List<Photographer> fakePhotographers(int count, int ratingsCount) {
    return fakeCollection(count, () -> fakePhotographer(() -> fakeRatings(ratingsCount)));
  }

  static Rating fakeRating() {
    return Rating.builder()
        .id(UUID.randomUUID().toString())
        .rate(faker.number().numberBetween(1, 10))
        .comment(faker.shakespeare().hamletQuote())
        .build();
  }

  static List<Rating> fakeRatings(int count) {
    return fakeCollection(count, Fakes::fakeRating);
  }

  static <T> List<T> fakeCollection(int count, Supplier<T> supplier) {
    return IntStream.range(1, randomIntIncluding(count))
        .mapToObj(ignored -> supplier.get())
        .collect(Collectors.toList());
  }

  private static int randomIntIncluding(int num) {
    return randomIntExcluding(num) + 1;
  }

  private static int randomIntExcluding(int num) {
    return (int) (Math.random() * num);
  }

  private static <T> T choose(List<T> objects) {
    return objects.get(randomIntExcluding(objects.size()));
  }
}
