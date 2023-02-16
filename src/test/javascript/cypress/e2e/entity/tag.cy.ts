import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Tag e2e test', () => {
  const tagPageUrl = '/tag/tag';
  const tagPageUrlPattern = new RegExp('/tag/tag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tagSample = { tag: 'Georgia Nevada Implementation', parentServer: 'dynamic invoice Handmade' };

  let tag;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/tag/api/tags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/tag/api/tags').as('postEntityRequest');
    cy.intercept('DELETE', '/services/tag/api/tags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/tag/api/tags/${tag.id}`,
      }).then(() => {
        tag = undefined;
      });
    }
  });

  it('Tags menu should load Tags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tag/tag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tag').should('exist');
    cy.url().should('match', tagPageUrlPattern);
  });

  describe('Tag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tag page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tag/tag/new$'));
        cy.getEntityCreateUpdateHeading('Tag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/tag/api/tags',
          body: tagSample,
        }).then(({ body }) => {
          tag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/tag/api/tags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/tag/api/tags?page=0&size=20>; rel="last",<http://localhost/services/tag/api/tags?page=0&size=20>; rel="first"',
              },
              body: [tag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tag');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tagPageUrlPattern);
      });

      it('edit button click should load edit Tag page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tagPageUrlPattern);
      });

      it('edit button click should load edit Tag page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tag');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tagPageUrlPattern);
      });

      it('last delete button click should delete instance of Tag', () => {
        cy.intercept('GET', '/services/tag/api/tags/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('tag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tagPageUrlPattern);

        tag = undefined;
      });
    });
  });

  describe('new Tag page', () => {
    beforeEach(() => {
      cy.visit(`${tagPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tag');
    });

    it('should create an instance of Tag', () => {
      cy.get(`[data-cy="tag"]`).type('Card multi-tasking compressing').should('have.value', 'Card multi-tasking compressing');

      cy.get(`[data-cy="textColor"]`).type('Wooden').should('have.value', 'Wooden');

      cy.get(`[data-cy="fillColor"]`).type('exploit Security Product').should('have.value', 'exploit Security Product');

      cy.get(`[data-cy="borderColor"]`).type('compress Market').should('have.value', 'compress Market');

      cy.get(`[data-cy="icon"]`).type('Nevada optimizing South').should('have.value', 'Nevada optimizing South');

      cy.get(`[data-cy="parentId"]`).type('40625').should('have.value', '40625');

      cy.get(`[data-cy="parentType"]`).type('primary Markets Montana').should('have.value', 'primary Markets Montana');

      cy.get(`[data-cy="parentServer"]`).type('strategy').should('have.value', 'strategy');

      cy.get(`[data-cy="parentUuid"]`)
        .type('532566ec-9d05-4c9a-9500-9ee7b413b60f')
        .invoke('val')
        .should('match', new RegExp('532566ec-9d05-4c9a-9500-9ee7b413b60f'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tag = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tagPageUrlPattern);
    });
  });
});
