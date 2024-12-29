insert into public.notification_type values (101, 'NT_issue_by_date'),(102, 'NT_issue_by_distance');

-- Table Status
INSERT INTO public.notification_type (id, name)
SELECT * FROM (VALUES
    (101, 'NT_issue_by_date'),
    (102, 'NT_issue_by_distance')
) AS new_notification_type(id, name)
WHERE NOT EXISTS (
    SELECT 1
    FROM public.notification_type
    LIMIT 1
);