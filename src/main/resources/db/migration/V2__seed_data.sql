-- V2__seed_data.sql

-- Organizations
INSERT INTO organizations (id, name, created_at, updated_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Org Alpha', now(), now()),
  ('22222222-2222-2222-2222-222222222222', 'Org Beta', now(), now());

-- Users
INSERT INTO users (id, organization_id, username, display_name, email, active, created_at, updated_at)
VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'alice', 'Alice Alpha', 'alice@orgalpha.com', true, now(), now()),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'bob', 'Bob Alpha', 'bob@orgalpha.com', true, now(), now()),
  ('cccccccc-cccc-cccc-cccc-cccccccccccc', '22222222-2222-2222-2222-222222222222', 'carol', 'Carol Beta', 'carol@orgbeta.com', true, now(), now());

-- Projects
INSERT INTO projects (id, organization_id, name, key, description, created_at, updated_at)
VALUES
  ('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 'Alpha Project', 'ALPHA', 'Sample project for Org Alpha', now(), now()),
  ('44444444-4444-4444-4444-444444444444', '22222222-2222-2222-2222-222222222222', 'Beta Project', 'BETA', 'Sample project for Org Beta', now(), now());

-- Issues
INSERT INTO issues (id, organization_id, project_id, assignee_id, title, description, status, priority, created_at, updated_at)
VALUES
  ('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', '33333333-3333-3333-3333-333333333333',
   'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'First Issue', 'This is a test issue for Org Alpha', 'OPEN', 'HIGH', now(), now()),
  ('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222', '44444444-4444-4444-4444-444444444444',
   'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Beta Issue', 'This is a test issue for Org Beta', 'OPEN', 'MEDIUM', now(), now());

-- Comments
INSERT INTO comments (id, organization_id, issue_id, author_id, body, created_at, updated_at)
VALUES
  ('77777777-7777-7777-7777-777777777777', '11111111-1111-1111-1111-111111111111', '55555555-5555-5555-5555-555555555555',
   'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'This is a comment on Alpha Issue', now(), now()),
  ('88888888-8888-8888-8888-888888888888', '22222222-2222-2222-2222-222222222222', '66666666-6666-6666-6666-666666666666',
   'cccccccc-cccc-cccc-cccc-cccccccccccc', 'This is a comment on Beta Issue', now(), now());
