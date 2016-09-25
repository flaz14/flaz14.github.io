#include <stdio.h>
#include <glib-object.h>
#include <gio/gio.h>

int
main ()
{
  printf ("Hi! I'm sample application!\n");

  GVolumeMonitor *volume_monitor = g_volume_monitor_get ();

  printf ("Call g_volume_monitor_get_connected_drives() ...\n");

  GList *connected_drives =
    g_volume_monitor_get_connected_drives (volume_monitor);

  GList *l, *ll;
  GMount *mount;

  GDrive *drive;
  GList *volumes;
  GVolume *volume;
  char *mount_uri, *identifier;
  GFile *root;


  for (l = connected_drives; l != NULL; l = l->next)
    {
      drive = l->data;
      volumes = g_drive_get_volumes (drive);
      if (volumes != NULL)
	{
	  for (ll = volumes; ll != NULL; ll = ll->next)
	    {
	      volume = ll->data;
	      identifier =
		g_volume_get_identifier (volume,
					 G_VOLUME_IDENTIFIER_KIND_CLASS);

	      // Skip network volumes
	      if (g_strcmp0 (identifier, "network") == 0)
		{
		  g_free (identifier);
		  continue;
		}
	      g_free (identifier);

	      mount = g_volume_get_mount (volume);
	      if (mount != NULL)
		{
		  root = g_mount_get_default_location (mount);
		  mount_uri = g_file_get_uri (root);
		}
	      g_object_unref (root);
	      g_object_unref (mount);
	      g_free (mount_uri);
	      g_object_unref (volume);
	    }
	  g_list_free (volumes);
	}
    }


  printf ("Bye!\n");
  g_list_free (connected_drives);
  g_object_unref (volume_monitor);


  return 0;
}
