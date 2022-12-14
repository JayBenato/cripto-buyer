package io.vertx.cryptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.cryptobuyer.repository.mappers.AccountMapper;
import io.vertx.cryptobuyer.repository.mappers.OrderMapper;
import io.vertx.cryptobuyer.repository.models.*;
import io.vertx.skeleton.orm.Repository;
import io.vertx.skeleton.orm.RepositoryHandler;
import io.vertx.skeleton.utils.VertxComponent;

public class Repositories extends VertxComponent {


  @Provides
  @Inject
  Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accountRepo(RepositoryHandler repositoryHandler) {
    return new Repository<>(AccountMapper.INSTANCE, repositoryHandler);
  }

  @Provides
  @Inject
  Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orderRepo(RepositoryHandler repositoryHandler) {
    return new Repository<>(OrderMapper.INSTANCE, repositoryHandler);
  }

}
